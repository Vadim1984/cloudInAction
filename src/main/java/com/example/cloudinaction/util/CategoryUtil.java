package com.example.cloudinaction.util;

import com.example.cloudinaction.dto.CategoryDto;
import com.example.cloudinaction.models.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class CategoryUtil {

    private static ConversionService conversionService;

    @Autowired
    @Qualifier("mvcConversionService")
    private ConversionService conversionServiceAutowired;

    @PostConstruct
    public void init() {
        CategoryUtil.conversionService = conversionServiceAutowired;
    }

    public static List<CategoryDto> convertCategoryListModelToListDto(List<Category> categories){
        return Optional.ofNullable(categories)
                .orElseGet(ArrayList::new)
                .stream()
                .map(category -> conversionService.convert(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
}
