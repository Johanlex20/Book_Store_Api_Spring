package com.bookStoreApiSpring.bookStoreApiSpring.controllers;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.BadRequestException;
import com.bookStoreApiSpring.bookStoreApiSpring.exceptions.ResourceNotFoundException;
import com.bookStoreApiSpring.bookStoreApiSpring.models.Book;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesItem;
import com.bookStoreApiSpring.bookStoreApiSpring.models.SalesOrder;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iBookRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iSalesItemRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.repository.iSalesOrderRepository;
import com.bookStoreApiSpring.bookStoreApiSpring.services.iServices.iStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.module.ResolutionException;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
public class HomeController {

    private final iBookRepository bookRepository;
    private final iSalesOrderRepository salesOrderRepository;
    private final iSalesItemRepository salesItemRepository;
    private final iStorageService storageService;

    @GetMapping(value = "/last-books")
    public List<Book> getLastBooks(){
        return bookRepository.findTop6ByOrderByCreatedAtDesc();
    }

    @GetMapping(value = "/books")
    public Page<Book> getBooks(@PageableDefault(sort = "title", direction = Sort.Direction.ASC, size = 5) Pageable pageable){
        return bookRepository.findAll(pageable);
    }

    @GetMapping(value = "/books/{slug}")
    public ResponseEntity<Book> getBookBySlug (@PathVariable(value = "slug") String slug){

        Book book = bookRepository
                .findBySlug(slug).orElseThrow(null);

        if (book != null){
            return ResponseEntity.ok(book);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/orders/{id}")
    public ResponseEntity<SalesOrder> getOrder(@PathVariable(value = "id") Integer id) {

        SalesOrder salesOrder = salesOrderRepository
                .findById(id)
                .orElse(null);

        if (salesOrder != null){
            return ResponseEntity.ok(salesOrder);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/orders/{orderId}/items/{itemId}/book/download")
    Resource downloadBookFromSalesItem(
            @PathVariable Integer orderId,
            @PathVariable Integer itemId
    ){
        SalesOrder salesOrder = salesOrderRepository
                .findById(orderId)
                .orElseThrow(ResolutionException::new);

        if (salesOrder.getPaymentStatus().equals(SalesOrder.PaymentStatus.PENDING)){
            throw new BadRequestException("La orden no ha sido pagado a un!");
        }

        SalesItem salesItem = salesItemRepository
                .findByIdAndOrder(itemId, salesOrder)
                .orElseThrow(ResourceNotFoundException::new);

        if (salesItem.getDownloadAvailable() > 0){

            salesItem.setDownloadAvailable(
                    salesItem.getDownloadAvailable() -1
            );
            salesItemRepository.save(salesItem);

            return storageService.loadAsResoure(salesItem.getBook().getFilePath()); //hay que importar de FileSystemStorageServices
        }else {
            throw new BadRequestException("El Archivo no se puede descargar mas de 3 veces!");
        }

    }

}
