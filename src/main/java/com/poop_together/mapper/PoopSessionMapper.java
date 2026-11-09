package com.poop_together.mapper;

import org.mapstruct.Mapper;

import com.poop_together.dto.response.poopSession.PoopSessionResponse;
import com.poop_together.entity.PoopSession;

@Mapper (componentModel = "spring")
public interface PoopSessionMapper {
    PoopSessionResponse toPoopSessionResponse(PoopSession req);
}
