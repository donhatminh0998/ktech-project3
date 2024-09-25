package com.example.ktech_project_3.request;

import com.example.ktech_project_3.request.dto.CloseRequestDto;
import com.example.ktech_project_3.request.dto.InspectOpenDto;
import com.example.ktech_project_3.request.dto.OpenRequestDto;
import com.example.ktech_project_3.request.dto.OpenRequestView;
import com.example.ktech_project_3.request.entity.CloseRequest;
import com.example.ktech_project_3.request.entity.OpenRequest;
import com.example.ktech_project_3.request.repo.CloseRequestRepository;
import com.example.ktech_project_3.request.repo.OpenRequestRepository;
import com.example.ktech_project_3.shop.entity.ShopEntity;
import com.example.ktech_project_3.shop.repo.ShopRepository;
import com.example.ktech_project_3.user.entity.UserEntity;
import com.example.ktech_project_3.user.repo.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.ktech_project_3.request.dto.OpenRequestView.fromEntity;

@Builder
@Service
@Slf4j
@RequiredArgsConstructor
public class RequestService {
    private final ShopRepository shopRepository;
    private final OpenRequestRepository openRepository;
    private final CloseRequestRepository closeRepository;
    private final UserRepository userRepository;

    public OpenRequestView openShop(
            String username,
            OpenRequestDto openDto){
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OpenRequest openRequest1 = new OpenRequest();
     /*   if(!(user.getRole().equals("ROLE_USER,OPEN.REQUEST,ORDER,READ.REQUEST"))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }*/
        if(openRepository.existsByRequestStatusAndUserId("PENDING",user.getId())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,"User already has a pending request.");
        }

        openRequest1.setUser(user);
        openRequest1.setShopName(openDto.getShopName());
        openRequest1.setDescription(openDto.getDescription());
        openRequest1.setCategory(openDto.getCategory());
        openRequest1.setRequestStatus("PENDING");
        openRequest1.setCreatedAt(LocalDateTime.now());
        openRepository.save(openRequest1);
        return fromEntity(openRequest1);
    }
    public OpenRequestView readOneRequest(
            Long requestId,
            String username
    ){
        OpenRequest openRequest= openRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        String username2 =  openRequest.getUser().getUsername();
        if(!(username.equals("admin") || username.equals(username2))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return  fromEntity(openRequest);
    }
    public List<OpenRequestView> readAllRequest(){
        List<OpenRequest> openRequests = openRepository.findAllOrderedByStatusAndCreatedAt();
        List<OpenRequestView> openRequestViews = new ArrayList<>();
        for (OpenRequest o : openRequests){
            openRequestViews.add(fromEntity(o));
        }
        return openRequestViews;
    }
    public OpenRequestView openConfirm(
            Long requestId,
            InspectOpenDto inspectDto
    ) {
        OpenRequest openRequest = openRepository.findById(requestId)
                .orElseThrow();
        UserEntity user = openRequest.getUser();
        log.info(user.toString());
        if (inspectDto.isApproved()) {
            openRequest.setRequestStatus("ACCEPTED");
            openRequest.setProcessedAt(LocalDateTime.now());
            openRepository.save(openRequest);
            log.info(openRequest.getUser().toString());

            if (openRequest.getRequestStatus().equals("ACCEPTED")) {
                String updatedRoles = "ROLE_BUSINESS,VIEW,ORDER";
                user.setRole(updatedRoles);
                userRepository.save(user);
                log.info(user.toString());

                ShopEntity shop = ShopEntity.builder()
                        .shopName(openRequest.getShopName())
                        .description(openRequest.getDescription())
                        .category(openRequest.getCategory())
                        .shopStatus("ACTIVE")
                        .user(openRequest.getUser())
                        .build();
                shopRepository.save(shop);
                log.info(shop.getUser().toString());
            }
        } else {
            if (inspectDto.getRejectionReason() != null) {
                openRequest.setRejectionReason(inspectDto.getRejectionReason());
            }
            openRequest.setRequestStatus("REJECTED");
            openRequest.setProcessedAt(LocalDateTime.now());
            openRepository.save(openRequest);
        }
        return  fromEntity(openRequest);
    }

    public CloseRequestDto closerShop(
            String username,
            String reason
    ){
        UserEntity user = userRepository.findByUsername(username).orElseThrow();
        CloseRequest closeRequest = new CloseRequest();

        closeRequest.setShop(user.getShop());
        closeRequest.setStatus("PENDING");
        closeRequest.setReason(reason);
        closeRequest.setCreatedAt(LocalDateTime.now());
        closeRepository.save(closeRequest);
        return  CloseRequestDto.formEntity(closeRequest);

    }
    public CloseRequestDto closeConfirm(
            Long closeId
    ){
        CloseRequest closeRequest = closeRepository.findById(closeId).orElseThrow();
        closeRequest.setStatus("ACCEPTED");
        closeRequest.setProcessedAt(LocalDateTime.now());
        closeRepository.save(closeRequest);
        if(closeRequest.getStatus().equals("ACCEPTED")){
            UserEntity user = closeRequest.getShop().getUser();
            user.setRole("ROLE_USER,VIEW,ORDER,READ.REQUEST");
            userRepository.save(user);
            ShopEntity shop = closeRequest.getShop();
            shop.setShopStatus("CLOSE");
            shopRepository.save(shop);
        }
        return CloseRequestDto.formEntity(closeRequest);
    }
    public CloseRequestDto readOneClose(
            Long closeId,
            String username1
    ){
        CloseRequest closeRequest = closeRepository.findById(closeId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        String username2 =  closeRequest.getShop().getUser().getUsername();
        if(!(username1.equals("admin") || username1.equals(username2))){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return  CloseRequestDto.formEntity(closeRequest);
    }
    public List<CloseRequestDto> readAllClose(){
        List<CloseRequest> closeRequests = closeRepository.findAllOrStatusAndCreatedAt();
        List<CloseRequestDto> closeRequestDtos = new ArrayList<>();
        for (CloseRequest o : closeRequests){
            closeRequestDtos.add(CloseRequestDto.formEntity(o));
        }
        return closeRequestDtos;
    }
}
