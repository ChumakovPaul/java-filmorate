package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.MpaRequest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @PostMapping
    public MpaDto create(@Valid @RequestBody MpaRequest request) {
        log.info("\nСоздание рейтинга {}", request);
        return mpaService.create(request);
    }

    @GetMapping
    public List<MpaDto> getAll() {
        log.info("\nВывод всех рейтингов");
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public MpaDto getMpa(@PathVariable long id) {
        log.info("\nВывод рейтинга id={}", id);
        return mpaService.getMpaById(id);
    }

    @PutMapping("/{id}")
    public MpaDto update(@PathVariable long id, @Valid @RequestBody MpaRequest request) {
        log.info("\nОбновление рейтинга id={}", id);
        return mpaService.update(id, request);
    }
}