package com.eshop.backend.services;

import com.eshop.backend.exceptions.NotFoundException;
import com.eshop.backend.exceptions.ShopAlreadyExistsException;
import com.eshop.backend.models.Shop;
import com.eshop.backend.models.User;
import com.eshop.backend.repositories.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserService userService;

    public Shop createShop(String tin, String brandName, String owner, User user) {
        this.assertTinDoesNotExist(tin);

        Shop shop = new Shop();
        shop.setTin(tin);
        shop.setBrandName(brandName);
        shop.setOwner(owner);
        shop.setUser(user);

        return this.shopRepository.save(shop);
    }

    public Shop findByCurrentUser() {
        User user = this.userService.getCurrentUser();

        return this.shopRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Shop not found for user"));
    }

    private void assertTinDoesNotExist(String tin) {
        if (this.shopRepository.existsByTin(tin)) {
            throw new ShopAlreadyExistsException(tin);
        }
    }
}
