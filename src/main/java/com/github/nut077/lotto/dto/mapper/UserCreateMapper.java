package com.github.nut077.lotto.dto.mapper;

import com.github.nut077.lotto.dto.UserDto;
import com.github.nut077.lotto.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserCreateMapper extends BaseMapper<User, UserDto> {
}
