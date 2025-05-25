package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.MpaRequest;
import ru.yandex.practicum.filmorate.exception.DuplicateException;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.mappers.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {

    @Autowired
    private final MpaDbStorage mpaDbStorage;

    public MpaDto create(MpaRequest request) {
        if (mpaDbStorage.findByName(request.getName()).isPresent()) {
            log.warn("\nРейтинг уже существует {}", request);
            throw new DuplicateException("Рейтинг " + request.getName() + " уже существует");
        }
        Mpa mpa = MpaMapper.mapToMpa(request);
        return MpaMapper.mapToMpaDto(mpaDbStorage.create(mpa));
    }

    public List<MpaDto> getAll() {
        return mpaDbStorage.getAll().stream().map(MpaMapper::mapToMpaDto).toList();
    }

    public MpaDto getMpaById(long id) {
        return MpaMapper.mapToMpaDto(mpaDbStorage.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Рейтинг id = " + id + " не найден ")));
    }

    public MpaDto update(long id, MpaRequest request) {
        mpaDbStorage.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Mpa id = " + id + " not found "));
        Mpa mpa;
        mpa = mpaDbStorage.findByName(request.getName()).orElse(null);
        if ((mpa != null) && (mpa.getId() != id)) {
            throw new DuplicateException("Имя " + request.getName() + " уже используется");
        }
        mpa = MpaMapper.mapToMpa(request);
        Mpa newMpa = new Mpa(id, mpa.getName());
        return MpaMapper.mapToMpaDto(mpaDbStorage.update(newMpa));
    }
}