package org.example.paymentservice.mapper;

import org.example.paymentservice.domain.Wallet;
import org.example.paymentservice.dto.WalletResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    WalletResponse walletToResponse(Wallet wallet);
}
