package com.example.spring5recipeapp.services;

import com.example.spring5recipeapp.domain.Recipe;
import com.example.spring5recipeapp.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@Service
public class ImageServiceImpl implements ImageService{
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
try{
    Recipe recipe=recipeRepository.findById(recipeId).get();
    Byte[] byteObjects=new Byte[file.getBytes().length];
    int i=0;
    for(byte b: file.getBytes()){
        byteObjects[i++]=b;
    }
    recipe.setImage(byteObjects);
    recipeRepository.save(recipe);
}catch (Exception e){
    log.error("Error occurred",e);
    e.printStackTrace();
}
    }
}
