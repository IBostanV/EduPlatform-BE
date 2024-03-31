package com.play.quiz.mapper;

import java.util.List;
import java.util.Objects;

import com.play.quiz.domain.Account;
import com.play.quiz.dto.AccountDto;
import lombok.SneakyThrows;
import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    private @Autowired PasswordEncoder passwordEncoder;

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", source = "accountId")
    @Mapping(target = "isEnabled", source = "enabled")
    @Mapping(target = "createdById", source = "createdBy.accountId")
    @Mapping(target = "updatedById", source = "updatedBy.accountId")
    @Mapping(target = "updatedByName", source = "updatedBy.username")
    @Mapping(target = "createdByName", source = "createdBy.username")
    public abstract AccountDto toDto(final Account account);

    @IterableMapping(qualifiedByName = "mappingFields")
    public abstract List<AccountDto> toDtoList(final List<Account> account);

    @Mapping(target = "accountId", source = "id")
    @Mapping(target = "password", source = "password", qualifiedByName = "handlePassword")
    @Mapping(target = "createdBy.accountId", source = "createdById")
    @Mapping(target = "updatedBy.accountId", source = "updatedById")
    public abstract Account toEntity(final AccountDto accountDto);

    @Mapping(target = "accountId", source = "id")
    @Mapping(target = "password", source = "password", qualifiedByName = "handlePassword")
    @Mapping(target = "avatar", expression = "java(handleAvatar(avatar))")
    @Mapping(target = "isEnabled", source = "enabled")
    @Mapping(target = "createdBy.accountId", source = "createdById")
    @Mapping(target = "updatedBy.accountId", source = "updatedById")
    @Mapping(target = "friends", ignore = true)
    public abstract Account toEntity(final AccountDto accountDto, @Context final MultipartFile avatar);

    @Named("handlePassword")
    protected char[] handlePassword(final char[] password) {
        return Objects.nonNull(password) ? passwordEncoder.encode(new String(password)).toCharArray() : null;
    }

    @SneakyThrows
    @Named("handleAvatar")
    protected byte[] handleAvatar(final MultipartFile avatar) {
        return Objects.nonNull(avatar) ? avatar.getBytes() : null;
    }

    @Named("mappingFields")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", source = "accountId")
    @Mapping(target = "isEnabled", source = "enabled")
    protected abstract AccountDto withoutAnswers(final Account account);
}
