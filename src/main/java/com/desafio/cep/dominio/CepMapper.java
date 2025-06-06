package com.desafio.cep.dominio;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CepMapper {

    CepMapper INSTANCE = Mappers.getMapper(CepMapper.class);

    CepVo toVo(Cep cep);

    Cep toDocument(CepVo vo);
}
