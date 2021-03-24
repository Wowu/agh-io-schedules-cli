package parser;

import collision.CollisionReason;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("unused")
public class Meeting {
    private final Conference conference;
    private final LocalDateTime dateStart;
    private final LocalDateTime dateEnd;
    private final String subject;
    private final String group;
    private final String lecturer;
    private final MeetingType type;
    private final Integer lengthInHours;
    private final MeetingFormat format;
    private final String room;

    private Meeting(MeetingBuilder meetingBuilder) {
        this.conference = meetingBuilder.conference;
        this.dateStart = meetingBuilder.dateStart;
        this.dateEnd = meetingBuilder.dateEnd;
        this.subject = meetingBuilder.subject;
        this.group = meetingBuilder.group;
        this.lecturer = meetingBuilder.lecturer;
        this.type = meetingBuilder.type;
        this.lengthInHours = meetingBuilder.lengthInHours;
        this.format = meetingBuilder.format;
        this.room = meetingBuilder.room;
    }

    public static final class MeetingBuilder {
        private Conference conference;
        private LocalDateTime dateStart;
        private LocalDateTime dateEnd;
        private String subject;
        private String group;
        private String lecturer;
        private MeetingType type;
        private Integer lengthInHours;
        private MeetingFormat format;
        private String room;

        public MeetingBuilder conference(Conference conference) {
            this.conference = conference;
            return this;
        }

        public MeetingBuilder dateStart(LocalDateTime dateStart) {
            this.dateStart = dateStart;
            return this;
        }

        public MeetingBuilder dateEnd(LocalDateTime dateEnd) {
            this.dateEnd = dateEnd;
            return this;
        }

        public MeetingBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MeetingBuilder group(String group) {
            this.group = group;
            return this;
        }

        public MeetingBuilder lecturer(String lecturer) {
            this.lecturer = lecturer;
            return this;
        }

        public MeetingBuilder type(String type) {
            this.type = MeetingType.getTypeFromString(type);
            return this;
        }

        public MeetingBuilder lengthInHours(Integer lengthInHours) {
            this.lengthInHours = lengthInHours;
            return this;
        }

        public MeetingBuilder format(String format) {
            this.format = MeetingFormat.getFormatFromString(format);
            return this;
        }

        public MeetingBuilder room(String room) {
            this.room = room;
            return this;
        }

        public Meeting build() {
            return new Meeting(this);
        }

    }

    /***
     * Two time periods P1 and P2 overlaps if, and only if, at least one of these conditions hold:
     *
     * P1 starts between the start and end of P2 (P2.from <= P1.from <= P2.to)
     * P2 starts between the start and end of P1 (P1.from <= P2.from <= P1.to)
     */
    public boolean compareMeeting(Meeting otherMeeting, StringBuilder result) {
        boolean overlap1 = !otherMeeting.getDateStart().isAfter(dateStart) && !dateStart.isAfter(otherMeeting.getDateEnd());
        boolean overlap2 = !dateEnd.isAfter(otherMeeting.getDateStart()) && !otherMeeting.getDateStart().isAfter(dateEnd);
        if (overlap1 || overlap2) {
            CollisionReason lecturerOverlap = lecturer.equals(otherMeeting.getLecturer()) ? CollisionReason.LECTURER : null;
            CollisionReason roomOverlap = room.equals(otherMeeting.getRoom()) ? CollisionReason.ROOM : null;
            CollisionReason groupOverlap = group.equals(otherMeeting.getGroup()) ? CollisionReason.GROUP : null;
            StringBuilder reasons = new StringBuilder();

            for (CollisionReason reason : new CollisionReason[]{lecturerOverlap, roomOverlap, groupOverlap}) {
                if (reason != null)
                    reasons.append(" ").append(CollisionReason.valueOf(reason.name()));
            }

            if (!reasons.isEmpty()) {
                result.append(String.format("\n%25s ", "-"))
                        .append(otherMeeting.toString())
                        .append("[POWODY:")
                        .append(reasons).append("]");
            }

            return !reasons.isEmpty();
        }

        return false;
    }

    public String toString() {
        return dateStart.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
                + "-" + dateEnd.format(DateTimeFormatter.ofPattern("HH:mm"))
                + " - " + subject + " - " + lecturer + " - Sala " + room;
    }

    public Conference getConference() {
        return conference;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
    }

    public String getLecturer() {
        return lecturer;
    }

    public MeetingType getType() {
        return type;
    }

    public Integer getLengthInHours() {
        return lengthInHours;
    }

    public MeetingFormat getFormat() {
        return format;
    }

    public String getRoom() {
        return room;
    }
}
