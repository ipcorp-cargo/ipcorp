package kz.ipcorp.controller;

import kz.ipcorp.model.entity.Status;
import kz.ipcorp.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final StatusService statusService;
    @GetMapping
    public String test() {
        return "admin";
    }

    @GetMapping("/statuses")
    public ResponseEntity<List<Status>> getStatus(){
        return new ResponseEntity<>(statusService.getStatus(), HttpStatus.OK);
    }
}
