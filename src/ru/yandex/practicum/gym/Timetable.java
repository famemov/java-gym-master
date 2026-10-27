package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<Integer, List<TrainingSession>>> timetable = new HashMap<>();
    // убрал раздваивание и соединил в единый hashmap

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();

        // убрал компаратор тк не увидел что он уже был переопределен
        TimeOfDay time = trainingSession.getTimeOfDay();
        int minutes = time.getHours() * 60 + time.getMinutes();

        if (!timetable.containsKey(dayOfWeek)) {
            timetable.put(dayOfWeek, new TreeMap<>());
        }

        TreeMap<Integer, List<TrainingSession>> daySessions =
                timetable.get(dayOfWeek);

        if (!daySessions.containsKey(minutes)) {
            daySessions.put(minutes, new ArrayList<>());
        }

        daySessions.get(minutes).add(trainingSession);
    }

    public TreeMap<Integer, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        if (timetable.containsKey(dayOfWeek)) {
            return timetable.get(dayOfWeek);
        } else {
            return new TreeMap<>(); // тут подправил все на treemap
        }
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        if (!timetable.containsKey(dayOfWeek)) {
            return List.of();
        }

        int minutes = timeOfDay.getHours() * 60 + timeOfDay.getMinutes();

        TreeMap<Integer, List<TrainingSession>> sessions =
                timetable.get(dayOfWeek);

        return sessions.getOrDefault(minutes, List.of());
    }

    public Map<Coach, Integer> getCountByCoaches () {
        Map<Coach, Integer> coaches = new HashMap<>();

        for (TreeMap<Integer, List<TrainingSession>> perDay: timetable.values()) {
            for (List<TrainingSession> sessionsForDay: perDay.values()) {
                for (TrainingSession session: sessionsForDay) {
                    Coach coach = session.getCoach();
                    if (coaches.containsKey(coach)) {
                        coaches.put(coach, coaches.getOrDefault(coach, 0) + 1);
                    } else  {
                        coaches.put(coach, 1);
                    }
                }
            }
        }

        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(coaches.entrySet());
        entries.sort(Map.Entry.<Coach, Integer>comparingByValue().reversed());
        //вот тут я не смог сразу сам догадаться как надо reversed написать тк у меня выходило по возрастанию
        Map<Coach, Integer> result = new LinkedHashMap<>();

        for (Map.Entry<Coach, Integer> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;

    }
}
