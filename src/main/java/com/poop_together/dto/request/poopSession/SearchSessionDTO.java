package com.poop_together.dto.request.poopSession;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.poop_together.enums.SessionStatus;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
@FieldDefaults (level = AccessLevel.PRIVATE)
public class SearchSessionDTO implements Pageable {

    @Builder .Default
    @Min (value = 0, message = "Page number must be at least 0")
    int pageNumber = 0;
    
    @Builder .Default
    @Min (value = 1, message = "Page size must be at least 1")
    int pageSize = 10;

    @Builder.Default
    String sortBy = "started_at";

    @Builder.Default
    Sort.Direction sortDirection = Sort.Direction.ASC;

    SessionStatus status;

    Sort sort;

    @Override
    public long getOffset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOffset'");
    }

    @Override
    public Pageable next() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'next'");
    }

    @Override
    public Pageable previousOrFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'previousOrFirst'");
    }

    @Override
    public Pageable first() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'first'");
    }

    @Override
    public Pageable withPage(int pageNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withPage'");
    }

    @Override
    public boolean hasPrevious() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasPrevious'");
    }
    


}
