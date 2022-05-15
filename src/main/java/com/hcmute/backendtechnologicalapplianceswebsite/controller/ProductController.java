package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.dashboad.Chart;
import com.hcmute.backendtechnologicalapplianceswebsite.model.dashboad.TopCustomer;
import com.hcmute.backendtechnologicalapplianceswebsite.model.dashboad.TopProduct;
import com.hcmute.backendtechnologicalapplianceswebsite.model.dashboad.TotalSales;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.dashboard.ChartRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.dashboard.TopCustomerRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.dashboard.TopProductRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.dashboard.TotalSalesRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.utils.fileUtils.upload.FileUploadUtil;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Brand;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Category;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Product;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.BrandRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.CategoryRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@RestController
@RequestMapping("/api/technological_appliances/")
public class ProductController {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ChartRepository chartRepository;
    private final TopProductRepository topProductRepository;
    private final TopCustomerRepository topCustomerRepository;
    private final TotalSalesRepository totalSalesRepository;

    public ProductController(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository,ChartRepository chartRepository,TopProductRepository topProductRepository,TopCustomerRepository topCustomerRepository,TotalSalesRepository totalSalesRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.chartRepository=chartRepository;
        this.topProductRepository=topProductRepository;
        this.topCustomerRepository=topCustomerRepository;
        this.totalSalesRepository=totalSalesRepository;
    }

    // Get All Products
    @GetMapping(value = "/products")
    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private String GetFileNameImg(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileDownloadUri = "http://localhost:8080/downloadFile/" + fileName;
        long size = file.getSize();

        FileUploadUtil.saveFile(fileName, file);

        return fileName;
    }

    // Get all product by brand
    @GetMapping(value = "/products/brand/{brandId}")
    public Iterable<Product> getAllProductsByBrand(@PathVariable String brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
        return productRepository.findAllByBrand(brand);
    }

    //    Create product
    @PostMapping(value = "/products", consumes = {"multipart/form-data"})
    public Product createProduct(Product product, @RequestParam(value = "files", required = false) MultipartFile[] uploadedFiles) {
        StringBuilder ImgName = new StringBuilder();
        for (var file : uploadedFiles) {
            String tempName = GetFileNameImg(file);
            ImgName.append(tempName).append("//");
        }
        //  Default value for productId
        product.setProductId(productRepository.generateProductId());

        product.setImage(ImgName.toString());

        Brand brand = brandRepository.findByBrandId("product.getBrand().getBrandId()");
        if (brand == null)
            throw new ResourceNotFoundException("Brand not found with id: " + product.getBrand().getBrandId());
        product.setBrand(brand);

        Category category = categoryRepository.findByCategoryId(product.getCategory().getCategoryId());
        if (category == null)
            throw new ResourceNotFoundException("Category not found with id: " + product.getCategory().getCategoryId());
        product.setCategory(category);

        if (product.getSaleDate() == null) {
            product.setSaleDate(null);
        }

        return productRepository.save(product);
    }

    // Get top products newest
    @GetMapping(value = "/products/newest/{quantity}")
    public List<Product> getTopProductsNewest(@PathVariable Integer quantity) {
        return productRepository.findAll(Sort.by("saleDate", "ProductId").descending()).subList(0, quantity);
    }
    @GetMapping(value = "/products/topseller")
    public Iterable<Product> getTopSeller() {
        return productRepository.gettopseller();
    }
    @GetMapping(value = "/products/topfeature")
    public Iterable<Product> getTopFeature() {
        return productRepository.gettopfeature();
    }
    @GetMapping(value = "/products/hottrend")
    public Iterable<Product> getHotTrend() {
        return productRepository.gethottrend();
    }
    // Get min price
    @GetMapping(value = "/products/price/min")
    public Double getMinPrice() {
        List<Product> products = productRepository.findAll();
        Double minPrice = products.get(0).getPrice();
        for (Product product : products) {
            if (product.getPrice() < minPrice)
                minPrice = product.getPrice();
        }
        return minPrice;
    }

    // Get max price
    @GetMapping(value = "/products/price/max")
    public Double getMaxPrice() {
        List<Product> products = productRepository.findAll();
        Double maxPrice = products.get(0).getPrice();
        for (Product product : products) {
            if (product.getPrice() > maxPrice)
                maxPrice = product.getPrice();
        }
        return maxPrice;
    }

    //    Get product by id
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        Product product = productRepository.findByProductId(id);
        if (product == null)
            throw new ResourceNotFoundException("Product not found with id: " + id);
        return ResponseEntity.ok(product);
    }

    //    Update product
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, Product product, @RequestParam("files") MultipartFile[] files) {
        Product _product = productRepository.findByProductId(id);
        if (_product == null)
            throw new ResourceNotFoundException("Product not found with id: " + id);
        if (files == null) {
            _product.setImage(product.getImage());
        } else {
            StringBuilder ImgName = new StringBuilder();
            for (var file : files) {
                String tempName = GetFileNameImg(file);
                ImgName.append(tempName).append("//");
            }
            _product.setImage(ImgName.toString());
        }
        _product.setName(product.getName());
        _product.setPrice(product.getPrice());
        _product.setDescription(product.getDescription());
        _product.setCategory(product.getCategory());
        _product.setQuantity(product.getQuantity());
        _product.setBackCam(product.getBackCam());
        _product.setFrontCam(product.getFrontCam());
        _product.setRam(product.getRam());
        _product.setRom(product.getRom());
        _product.setWeight(product.getWeight());
        _product.setScreen(product.getScreen());
        _product.setOs(product.getOs());
        _product.setBattery(product.getBattery());
        _product.setCpu(product.getCpu());
        _product.setSaleDate(product.getSaleDate());
        _product.setVga(product.getVga());
        productRepository.save(_product);
        return ResponseEntity.ok(_product);
    }

    //    Delete product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable String id) {
        Product product = productRepository.findByProductId(id);
        if (product == null)
            throw new ResourceNotFoundException("Product not found with id: " + id);
        productRepository.delete(product);
        return ResponseEntity.ok(product);
    }
    @GetMapping("/chartdata")
    public Iterable<Chart>MonthToTal(){
        return chartRepository.getTotalByMonth();
    }
    @GetMapping("/topproduct")
    public Iterable<TopProduct>TopProducts(){return topProductRepository.getTopProduct();}
    @GetMapping("/topcustomer")
    public Iterable<TopCustomer>TopCustomer(){return topCustomerRepository.getTopCustomer();}
    @GetMapping("/totallaptop")
    public Iterable<TotalSales>TotalLaptop(){
        try{
            return totalSalesRepository.totalLaptop();
        }catch (Exception e)
        {
            TotalSales a=new TotalSales(1,0);
            List<TotalSales>c=new ArrayList<>();
            c.add(a);
            Iterable<TotalSales>b=c;
            return b;
        }
    }
    @GetMapping("/totalmobile")
    public Iterable<TotalSales>TotalMobile(){
        try{
            return totalSalesRepository.totalMobile();
        }catch (Exception e)
        {
            TotalSales a=new TotalSales(2,0);
            List<TotalSales>c=new ArrayList<>();
            c.add(a);
            Iterable<TotalSales>b=c;
            return b;
        }
    }
    @GetMapping("/totalall")
    public Iterable<TotalSales>TotalAll(){
        try{
        return totalSalesRepository.totalAll();
    }
        catch (Exception e)
    {
        TotalSales a=new TotalSales(3,0);
        List<TotalSales>c=new ArrayList<>();
        c.add(a);
        Iterable<TotalSales>b=c;
        return b;
    }}

}
