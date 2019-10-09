package com.github.nut077.lotto.dto.mapper;

import com.github.nut077.lotto.dto.UserCreateDto;
import com.github.nut077.lotto.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserCreateMapper extends BaseMapper<User, UserCreateDto> {
}
