package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerOrderByStartDesc(User bookerFromDb);

    List<Booking> findAllByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User bookerFromDb, LocalDateTime nowDateTime, LocalDateTime nowDateTime1);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User bookerFromDb, LocalDateTime nowDateTime);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User bookerFromDb, LocalDateTime nowDateTime);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User bookerFromDb, BookingState bookingState);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(User bookerFromDb);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(User bookerFromDb, LocalDateTime nowDateTime, LocalDateTime nowDateTime1);

    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(User bookerFromDb, LocalDateTime nowDateTime);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(User bookerFromDb, LocalDateTime nowDateTime);

    List<Booking> findAllByItem_OwnerAndStatusEqualsOrderByStartDesc(User bookerFromDb, BookingState bookingState);
}