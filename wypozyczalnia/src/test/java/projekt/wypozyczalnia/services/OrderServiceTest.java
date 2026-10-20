package projekt.wypozyczalnia.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projekt.wypozyczalnia.data.dtos.OrderDTO;
import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.data.dtos.ProductReservationDto;
import projekt.wypozyczalnia.data.entities.*;
import projekt.wypozyczalnia.exceptions.ConflictReservationException;
import projekt.wypozyczalnia.jwt.JwtUtils;
import projekt.wypozyczalnia.mappers.OrderMapper;
import projekt.wypozyczalnia.mappers.ProductMapper;
import projekt.wypozyczalnia.repositories.*;
import projekt.wypozyczalnia.requests.OrderStatusChangeRequest;
import projekt.wypozyczalnia.services.CartService;
import projekt.wypozyczalnia.services.MailService;
import projekt.wypozyczalnia.services.ProductReservationService;

import jakarta.persistence.EntityNotFoundException;
import projekt.wypozyczalnia.servicesImpl.OrderServiceImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @Mock
    private ProductCartRepository productCartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderProductRepository orderProductRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private MailService mailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getClientOrders_ShouldReturnClientOrders() {
        // given
        int userId = 1;
        when(jwtUtils.getCurrentUserId()).thenReturn(userId);
        when(orderRepository.findByCartUserUserIdOrderByOrderDateDesc(userId))
                .thenReturn(Collections.singletonList(new Order()));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDTO());

        // when
        List<OrderDTO> orders = orderService.getClientOrders();

        // then
        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(1)).findByCartUserUserIdOrderByOrderDateDesc(userId);
    }

    @Test
    void makeOrder_ShouldCreateOrderSuccessfully() {
        // given
        int userId = 1;
        LocalDate rentalStart = LocalDate.now().plusDays(1);
        LocalDate rentalEnd = LocalDate.now().plusDays(5);

        Cart cart = new Cart();
        cart.setCartId(1);
        when(jwtUtils.getCurrentUserId()).thenReturn(userId);
        when(cartRepository.findByUserUserId(userId)).thenReturn(cart);
        when(productCartRepository.findById_CartId_CartId(cart.getCartId()))
                .thenReturn(Collections.emptyList());
        when(orderStatusRepository.findByOrderStatusId(1)).thenReturn(new OrderStatus());
        when(addressRepository.findByUserUserIdAndIsDefaultTrue(userId)).thenReturn(Optional.of(new Address()));

        // when
        orderService.makeOrder(rentalStart, rentalEnd);

        // then
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartService, times(1)).clearCart();
        verify(mailService, times(1)).sendMail(anyString(), anyString());
    }

    @Test
    void changeOrderStatus_ShouldUpdateOrderStatus() {
        // given
        int orderId = 1;
        int statusId = 2;

        OrderStatusChangeRequest request = new OrderStatusChangeRequest();
        request.setOrderId(orderId);
        request.setStatusId(statusId);

        Order order = new Order();
        order.setOrderId(orderId);

        Cart cart = new Cart();
        User user = new User();
        user.setEmail("test@example.com"); // Replace with realistic data
        cart.setUser(user);
        order.setCart(cart);

        OrderStatus status = new OrderStatus();
        status.setOrderStatusId(statusId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderStatusRepository.findByOrderStatusId(statusId)).thenReturn(status);

        // when
        orderService.changeOrderStatus(request);

        // then
        verify(orderRepository, times(1)).save(order);
        verify(mailService, times(1)).sendMail(anyString(), anyString(), anyString());
        assertEquals(status, order.getOrderStatus());
    }


    @Test
    void getOrderDetails_ShouldReturnOrderDetails() {
        // given
        int orderId = 1;
        Order order = new Order();
        OrderDTO orderDTO = new OrderDTO();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDTO);
        when(orderProductRepository.findById_OrderId(orderId)).thenReturn(Collections.emptyList());

        // when
        var result = orderService.getOrderDetails(orderId);

        // then
        assertNotNull(result);
        assertEquals(orderDTO, result.getOrderDTO());
        assertTrue(result.getProductsDTO().isEmpty());
    }
}
