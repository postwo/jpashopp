package com.example.jpashopp.service;

import com.example.jpashopp.domain.items.ItemImg;
import com.example.jpashopp.repository.ItemImgRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemImgService {

    @Value("${itemImgLocation}")//application.yml에 설정한 “itemImgLocation” 값을 읽어온다
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드를 호출합니다. 호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }


    //수정
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        if (!itemImgFile.isEmpty()) {
            //상품 이미지 아이디를 이용하여 기존에 저장했던 상품 이미지 엔티티를 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            //기존 이미지 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) { //기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); //업데이트한 상품 이미지 파일을 업로드
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);/*변경된 상품 이미지 정보를 셋팅해줍니다. 여기서 중요한 점은 상품 등록 때처럼
            itemImgRepository.save() 로직을 호출하지 않는다는 것입니다. savedItemImg 엔티티는 현재 영속 상태이므로 데이터를 변경하는 것만으로 변경
            감지 기능이 동작하여 트랜잭션이 끝날 때 update 쿼리가 실행됩니다. 여기서 중요한 것은 엔티티가 영속 상태여야 한다는 것*/
        }
    }

}
