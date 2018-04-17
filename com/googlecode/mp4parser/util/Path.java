package com.googlecode.mp4parser.util;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Path {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static Pattern component = Pattern.compile("(....|\\.\\.)(\\[(.*)\\])?");

    private Path() {
    }

    public static String createPath(Box box) {
        return createPath(box, TtmlNode.ANONYMOUS_REGION_ID);
    }

    private static String createPath(Box box, String path) {
        Container parent = box.getParent();
        int index = 0;
        for (Box sibling : parent.getBoxes()) {
            if (sibling.getType().equals(box.getType())) {
                if (sibling == box) {
                    break;
                }
                index++;
            }
        }
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(String.format("/%s[%d]", new Object[]{box.getType(), Integer.valueOf(index)})));
        stringBuilder.append(path);
        path = stringBuilder.toString();
        if (parent instanceof Box) {
            return createPath((Box) parent, path);
        }
        return path;
    }

    public static <T extends Box> T getPath(Container container, String path) {
        List<T> all = getPaths(container, path, true);
        return all.isEmpty() ? null : (Box) all.get(0);
    }

    public static <T extends Box> List<T> getPaths(Container container, String path) {
        return getPaths(container, path, false);
    }

    private static <T extends Box> List<T> getPaths(Container container, String path, boolean singleResult) {
        return getPaths((Object) container, path, singleResult);
    }

    private static <T extends Box> List<T> getPaths(Box box, String path, boolean singleResult) {
        return getPaths((Object) box, path, singleResult);
    }

    private static <T extends Box> List<T> getPaths(Object thing, String path, boolean singleResult) {
        if (path.startsWith("/")) {
            String path2 = path.substring(1);
            while (thing instanceof Box) {
                thing = ((Box) thing).getParent();
            }
            path = path2;
        }
        if (path.length() != 0) {
            String later;
            if (path.contains("/")) {
                later = path.substring(path.indexOf(47) + 1);
                path2 = path.substring(0, path.indexOf(47));
            } else {
                path2 = path;
                later = TtmlNode.ANONYMOUS_REGION_ID;
            }
            Matcher m = component.matcher(path2);
            if (m.matches()) {
                String type = m.group(1);
                if ("..".equals(type)) {
                    if (thing instanceof Box) {
                        return getPaths(((Box) thing).getParent(), later, singleResult);
                    }
                    return Collections.emptyList();
                } else if (!(thing instanceof Container)) {
                    return Collections.emptyList();
                } else {
                    int index = -1;
                    if (m.group(2) != null) {
                        index = Integer.parseInt(m.group(3));
                    }
                    List<T> children = new LinkedList();
                    int currentIndex = 0;
                    for (Box box1 : ((Container) thing).getBoxes()) {
                        if (box1.getType().matches(type)) {
                            if (index == -1 || index == currentIndex) {
                                children.addAll(getPaths(box1, later, singleResult));
                            }
                            currentIndex++;
                        }
                        if ((singleResult || index >= 0) && !children.isEmpty()) {
                            return children;
                        }
                    }
                    return children;
                }
            }
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(path2));
            stringBuilder.append(" is invalid path.");
            throw new RuntimeException(stringBuilder.toString());
        } else if (thing instanceof Box) {
            return Collections.singletonList((Box) thing);
        } else {
            throw new RuntimeException("Result of path expression seems to be the root container. This is not allowed!");
        }
    }
}
