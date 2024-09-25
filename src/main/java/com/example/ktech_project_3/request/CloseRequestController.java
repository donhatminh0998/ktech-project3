package com.example.ktech_project_3.request;

import com.example.ktech_project_3.AuthenticationFacade;
import com.example.ktech_project_3.request.dto.CloseRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CloseRequestController {
    private final RequestService service;
    private final AuthenticationFacade authentication;


    @PostMapping ("/shop/closeRequest")
    public ResponseEntity<?> openShop(
            @RequestParam String reason) {
        String username = authentication.findUsername();

        try {
            if (reason == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Please provide a reason!!");
            }
            CloseRequestDto closeRequestDto  = service.closerShop(username,reason);
            return ResponseEntity.ok(closeRequestDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }
    @PostMapping("/admin/closeRequest/confirm/{closeId}")
    public ResponseEntity<?> closeConfirm(
            @PathVariable Long closeId
    ) {
        String username = authentication.findUsername();
        if (!username.equals("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to perform this action.");
        }
        try {
            CloseRequestDto closeRequestDto = service.closeConfirm(closeId);
            return ResponseEntity.ok(closeRequestDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @GetMapping("/read/closeRequest/{closeId}")
    public ResponseEntity<?> readOneClose (
            @PathVariable
            Long closeId

    ){
        String username = authentication.findUsername();
        try {
            CloseRequestDto closeRequestDto = service.readOneClose(closeId, username);
            return ResponseEntity.ok(closeRequestDto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
    @GetMapping("/admin/closeRequest/readAll")
    public List<CloseRequestDto> readAllRequest () {
        String username = authentication.findUsername();
        if(username.equals("admin")) {return service.readAllClose();}
        return null;
    }
}
