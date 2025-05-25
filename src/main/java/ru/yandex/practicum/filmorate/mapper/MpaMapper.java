package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.dto.MpaRequest;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static Mpa mapToMpa(MpaRequest request) {
        Mpa rating = new Mpa();
        rating.setName(request.getName());
        return rating;
    }

    public static MpaDto mapToMpaDto(Mpa rating) {
        MpaDto dto = new MpaDto();
        dto.setId(rating.getId());
        dto.setName(rating.getName());
        return dto;
    }
}