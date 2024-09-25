package com.example.ktech_project_3.request;

import com.example.ktech_project_3.AuthenticationFacade;
import com.example.ktech_project_3.request.dto.InspectOpenDto;
import com.example.ktech_project_3.request.dto.OpenRequestDto;
import com.example.ktech_project_3.request.dto.OpenRequestView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("openRequest")

public class OpenRequestController {
    private final RequestService service;
    private final AuthenticationFacade authentication;


    @PostMapping("/openRequest")
    public ResponseEntity<?> openShop(
            @RequestBody OpenRequestDto dto) {
        String username = authentication.findUsername();
        try {
            if (dto.getShopName() == null || dto.getDescription() ==null ||dto.getCategory() ==null ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Failed to create OpenRequest due to invalid data.");
            }
            OpenRequestView openRequestView = service.openShop(username, dto);
            return ResponseEntity.ok(openRequestView);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/read/openRequest/{requestId}")
    public ResponseEntity<?> readOneRequest (
            @PathVariable
            Long requestId
    ){
        String username = authentication.findUsername();
        try {
            OpenRequestView openRequestView = service.readOneRequest(requestId,username);
            return ResponseEntity.ok(openRequestView);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @GetMapping("/admin/openRequest/readAll")
    public List<OpenRequestView> readAllRequest () {
        String username = authentication.findUsername();
        if(username.equals("admin")) {return service.readAllRequest();}
        return null;
    }


    @PostMapping("/admin/openRequest/confirm/{requestId}")
    public ResponseEntity<?> confirmRequest(
            @PathVariable Long requestId,
            @RequestBody InspectOpenDto inspectDto
    ) {
        String username = authentication.findUsername();
        if (!username.equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to perform this action.");
        }

        try {
            OpenRequestView updatedRequest = service.openConfirm(requestId, inspectDto);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

}
