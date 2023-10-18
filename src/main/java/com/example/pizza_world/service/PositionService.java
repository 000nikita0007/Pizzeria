package com.example.pizza_world.service;

import com.example.pizza_world.bean.Position;
import com.example.pizza_world.bean.PositionOrderMap;
import com.example.pizza_world.bean.Size;
import com.example.pizza_world.dao.PositionDao;
import com.example.pizza_world.dao.PositionOrderMapDao;
import com.example.pizza_world.dao.SizeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class PositionService {

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private SizeDao sizeDao;

    @Autowired
    private PositionOrderMapDao positionOrderMapDao;

    public void savePosition(Position position) {
        if (position.getImageUrl()==null) {
            position.setImageUrl("/static/pizza.jpg");
        }
        positionDao.save(position);
    }

    public void deletePosition(Position position) {
        List<PositionOrderMap> positionOrderMapList = positionOrderMapDao.findAll();
        for (int i = 0; i < positionOrderMapList.size(); i++) {
            if (positionOrderMapList.get(i).getPosition().equals(position)) {
                positionOrderMapDao.delete(positionOrderMapList.get(i));
            }
        }
        positionDao.delete(position);
    }

    public void updatePosition(Position position) {
        if (position.getImageUrl()==null) {
            position.setImageUrl("/static/pizza.jpg");
        }
        positionDao.update(position);
    }

    public List<Position> findAllPositions() {
        return positionDao.findAll();
    }

    public Position findPositionById(int id) {
        return positionDao.findById(id, Position.class);
    }

    public List<Size> findSizes () {
        return sizeDao.findAll();
    }

    public String uploadImage(MultipartFile imageFile) {
        String fileName = imageFile.getOriginalFilename();
        Path filePath = Paths.get("src/main/resources/static", fileName);
        try {
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/static/" + fileName;
    }
}
