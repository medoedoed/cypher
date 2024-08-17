package utils;

import jline.console.completer.Completer;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ServiceNameCompleter implements Completer {
    private final SortedSet<String> serviceNames = new TreeSet<>();

    public ServiceNameCompleter(List<String> services) {
        serviceNames.addAll(services);
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        if (buffer == null) {
            candidates.addAll(serviceNames);
        } else {
            for (String match : serviceNames.tailSet(buffer)) {
                if (!match.startsWith(buffer)) {
                    break;
                }
                candidates.add(match);
            }
        }
        return candidates.isEmpty() ? -1 : 0;
    }
}
