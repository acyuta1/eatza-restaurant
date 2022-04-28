package com.eatza.restaurantsearch.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.eatza.restaurantsearch.model.MenuItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
	
	Page<MenuItem> findByNameContaining(String name, Pageable pageable);
	
	Optional<MenuItem> findById(Long id);
	Page<MenuItem> findByMenu_id(Long id, Pageable pageable);

	@Query(value = "select m.* from menu_items m where m.id in :menuIds", nativeQuery = true)
	List<MenuItem> findByIds(@Param("menuIds") Set<Long> menuIds);

}
