package com.example.jpashopp.domain.items;

import com.example.jpashopp.dto.ItemSearchDto;
import com.example.jpashopp.dto.MainItemDto;
import com.example.jpashopp.dto.QMainItemDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


/*ItemRepositoryCustom 인터페이스를 구현하는 ItemRepositoryCustomImpl 클래스를 작성합니다.
이때 주의할 점으로 클래스명 끝에 “Impl”을 붙여주어야 정상적으로 동작합니다.
Querydsl에서는 BooleanExpression이라는 where절에서 사용할 수 있는 값을 지원*/


public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory; // 동적으로 쿼리를 생성하기 위해 JPAQueryFactory 클래스를 사용

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        //상품 판매 조건이 전체(null)일 경우는 null를 리턴합니다. 결과값이 null이면 where절에서 해당 조건은 무시
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        //searchDateType의 값에 따라서 dateTime의 값을 이전의 시간으로 셋팅 후 해당 시간 이후로 등록된 상품만 조회
        LocalDateTime dateTime = LocalDateTime.now();

        switch (searchDateType) {
            case "1d" : dateTime = dateTime.minusDays(1); break;
            case "1w" : dateTime = dateTime.minusWeeks(1); break;
            case "1m" : dateTime = dateTime.minusMonths(1); break;
            case "6m" : dateTime = dateTime.minusMonths(6); break;
            default: return null; //all, null
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        //searchBy의 값에 따라서 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환
        switch (searchBy) {
            case "itemNm"   : return QItem.item.itemNm.like("%" + searchQuery + "%");
            case "createBy" : return QItem.item.createdBy.like("%" + searchQuery + "%");
            default: return null;
        }
    }


    //이러한 방식도 있다

//    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
//        if (searchBy == null || searchQuery == null) {
//            throw new IllegalArgumentException("searchBy와 searchQuery는 null일 수 없습니다.");
//        }
//
//        switch (searchBy) {
//            case "itemNm":
//                return QItem.item.itemNm.like("%" + searchQuery + "%");
//            case "createBy":
//                return QItem.item.createdBy.like("%" + searchQuery + "%");
//            default:
//                throw new IllegalArgumentException("유효하지 않은 검색 조건: " + searchBy);
//        }
//    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = content.size();
        return new PageImpl<>(content, pageable, total);

        /*queryFactory를 이용해서 쿼리를 생성합니다. 쿼리문을 직접 작성할 때의 형태와 문법이 비슷한 것을 볼 수 있습니다.
        selectFrom(Qitem.item) : 상품 데이터를 조회하기 위해서 QItem의 item을 지정합니다.
        where 조건절 : BooleanExpression 반환하는 조건문들을 넣어줍니다. ‘,’ 단위로 넣어줄 경우 and 조건으로 인식합니다.
        offset : 데이터를 가지고 올 시작 인덱스를 지정합니다.
        limit : 한 번에 가지고 올 최대 개수를 지정합니다.
        fetch() : 조회 대상 리스트를 반환합니다*/

    }



    //
    private BooleanExpression itemNmLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"),
                        itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(item.count())
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"),
                        itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }




}