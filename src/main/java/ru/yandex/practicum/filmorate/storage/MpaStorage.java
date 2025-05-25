package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    Mpa create(Mpa mpa);

    Mpa update(Mpa mpa);

    void deleteMpa(Long id);

    List<Mpa> getAll();

    Optional<Mpa> findById(Long mpaId);

    Optional<Mpa> findByName(String name);
}