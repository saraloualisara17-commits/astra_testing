package com.wash.laundry_app.command;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommandeTapisMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productNom", source = "product.nom")
    @Mapping(target = "productPricingMethod", expression = "java(commandeTapis.getProduct() != null && commandeTapis.getProduct().getPricingMethod() != null ? commandeTapis.getProduct().getPricingMethod().name() : null)")
    @Mapping(target = "images", expression = "java(mapImages(commandeTapis.getImages()))")
    @Mapping(target = "surface", expression = "java(calculateSurface(commandeTapis))")
    CommandeTapisDTO toDto(CommandeTapis commandeTapis);

    default BigDecimal calculateSurface(CommandeTapis item) {
        if (item.getLargeur() != null && item.getHauteur() != null) {
            return item.getLargeur().multiply(item.getHauteur());
        }
        return null;
    }

    default List<CommandeImageDTO> mapImages(List<CommandeImage> images) {
        if (images == null) return java.util.Collections.emptyList();
        return images.stream()
                .map(img -> CommandeImageDTO.builder()
                        .imageUrl(img.getImageUrl())
                        .photoType(img.getPhotoType() != null ? img.getPhotoType().name() : null)
                        .build())
                .collect(Collectors.toList());
    }
}
