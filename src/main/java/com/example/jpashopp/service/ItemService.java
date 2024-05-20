package com.example.jpashopp.service;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemImg;
import com.example.jpashopp.dto.ItemFormDto;
import com.example.jpashopp.repository.ItemImgRepository;
import com.example.jpashopp.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
}
