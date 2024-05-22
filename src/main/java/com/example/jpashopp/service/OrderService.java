package com.example.jpashopp.service;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemImg;
import com.example.jpashopp.domain.members.Member;
import com.example.jpashopp.domain.orders.Order;
import com.example.jpashopp.domain.orders.OrderItem;
import com.example.jpashopp.dto.OrderDto;
import com.example.jpashopp.dto.OrderHistoryDto;
import com.example.jpashopp.dto.OrderItemDto;
import com.example.jpashopp.repository.ItemImgRepository;
import com.example.jpashopp.repository.ItemRepository;
import com.example.jpashopp.repository.MemberRepository;
import com.example.jpashopp.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email) {
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new); // 주문할 상품을 조회
        Member member = memberRepository.findByEmail(email); //현재 로그인한 회원의 이메일 정보를 이용해서 회원 정보를 조회

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());//주문할 상품 엔티티와 주문 수량을 이용하여 주문 상품 엔티티를 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }


    @Transactional(readOnly = true)
    public Page<OrderHistoryDto> getOrderList(String email, Pageable pageable) { //유저의 아이디와 페이징 조건을 이용하여 주문 목록을 조회

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);//countOrder(email) : 유저의 주문 총 개수를 구합

        List<OrderHistoryDto> orderHistoryDtos = new ArrayList<>();

        for (Order order : orders) {
            OrderHistoryDto orderHistoryDto = new OrderHistoryDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn(orderItem.getItem().getId(), "Y");

                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistoryDto.addOrderItemDto(orderItemDto);
            }

            orderHistoryDtos.add(orderHistoryDto);
        }

        return new PageImpl<OrderHistoryDto>(orderHistoryDtos, pageable, totalCount);
    }
}
