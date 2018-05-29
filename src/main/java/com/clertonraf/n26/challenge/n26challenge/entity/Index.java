package com.clertonraf.n26.challenge.n26challenge.entity;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Index {

    private final ZonedDateTime zonedDateTime;
    private final String uuid;

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }

    public Index(ZonedDateTime zonedDateTime) {
        this.zonedDateTime = zonedDateTime;
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    public int hashCode() {
        return 553 + Objects.hashCode(this.uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Index other = (Index) obj;

        if (!Objects.equals(this.zonedDateTime, other.zonedDateTime)) {
            return false;
        }
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }

        return true;
    }
}

