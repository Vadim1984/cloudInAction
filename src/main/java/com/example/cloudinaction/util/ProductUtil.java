package com.example.cloudinaction.util;

import com.example.cloudinaction.dto.ProductDto;
import com.example.cloudinaction.models.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
public class ProductUtil {

    private static ConversionService conversionService;

    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionServiceAutowired;

    @PostConstruct
    public void init() {
        ProductUtil.conversionService = conversionServiceAutowired;
    }

    public static List<ProductDto> convertListModelToListDto(List<Product> products){
        return Optional.ofNullable(products)
                .orElseGet(ArrayList::new)
                .stream()
                .map(product -> conversionService.convert(product, ProductDto.class))
                .collect(Collectors.toList());
    }
}
