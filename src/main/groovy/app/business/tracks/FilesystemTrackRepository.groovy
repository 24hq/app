package app.business.tracks

import com.google.common.base.Charsets
import com.google.common.io.Files
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import javax.annotation.PostConstruct

import static java.util.Arrays.stream

@Repository
class FilesystemTrackRepository implements TrackRepository {

    private static final Yaml yaml = new Yaml(new Constructor(Track))

    @Value('${track.rootDir:classpath:tracks}')
    private final File trackRootDir

    private final List<Track> tracks = []

    @PostConstruct
    def warmup() {
        stream(trackRootDir.listFiles()).forEach({ file ->
            def reader = Files.newReader(file, Charsets.UTF_8)
            def track = (Track) yaml.load(reader)
            tracks << track
        })
    }

    @Override
    Page<Track> list(Pageable pageable) {
        new PageImpl<Track>(tracks)
    }

    @Override
    Track findByCode(String code) {
        tracks.find { it.code == code }
    }
}
