package com.example.distributed_database_project_server.product.brand;

import jakarta.validation.constraints.NotBlank;

class CreateBrandForm {

    @NotBlank(message = "Brand name should not be blank")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
