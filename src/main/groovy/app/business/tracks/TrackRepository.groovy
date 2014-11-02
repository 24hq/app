package app.business.tracks

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TrackRepository {

    Page<Track> list(Pageable pageable)

    Track findByCode(String code)

}
