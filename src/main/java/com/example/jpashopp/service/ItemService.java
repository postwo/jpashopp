package com.example.jpashopp.service;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemImg;
import com.example.jpashopp.dto.ItemFormDto;
import com.example.jpashopp.dto.ItemImgDto;
import com.example.jpashopp.dto.ItemSearchDto;
import com.example.jpashopp.repository.ItemImgRepository;
import com.example.jpashopp.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final  ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
        // 상품 등록
        Item item = itemFormDto.toEntity(itemFormDto);
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0, max = itemImgFileList.size(); i < max; i++) {
            ItemImg itemImg = ItemImg.builder()
                    .item(item)
                    .repimgYn(i == 0 ? "Y" : "N") // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 “Y”로 셋팅
                    .build();

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }


    //등록된 상품을 불러온다
    @Transactional(readOnly = true) //상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정합니다. 이럴 경우 JPA가 더티체킹(변경감지)을 수행하지 않아서 성능을 향상 시킬 수 있습니다.
    public ItemFormDto getItemDetail(Long itemId) {

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); //itemId에 해당하는 ItemImg 객체들을 ID 오름차순으로 조회하여 리스트에 저장
        List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //ItemImgDto 객체들을 저장할 빈 리스트를 생성

        for (ItemImg itemImg : itemImgList) { //itemImgList라는 리스트에 들어 있는 ItemImg 객체들을 하나씩 꺼내서 itemImg라는 변수에 담는다 //itemImgList에 3개의 ItemImg 객체가 있다면, 이 반복문은 3번 실행
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg); //ItemImgDto.of(itemImg)는 ItemImg 객체를 입력으로 받아서 ItemImgDto 객체를 생성하는 메서드
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new); //itemId에 해당하는 Item 객체를 조회하고, 만약 존재하지 않으면 EntityNotFoundException 예외를 발생
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }


    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto); //상품 등록 화면으로부터 전달 받은 ItemFormDto를 통해서 상품 엔티티를 업데이트

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for (int i = 0, max = itemImgFileList.size(); i < max; i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }


    //ItemService 클래스에 상품 조회 조건과 페이지 정보를 파라미터로 받아서 상품 데이터를 조회하는 getAdminItemPage() 메소드를 추가
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }


}
