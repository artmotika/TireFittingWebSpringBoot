package com.web.tirefitting.repositories;

import com.web.tirefitting.models.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Long>  {
}
