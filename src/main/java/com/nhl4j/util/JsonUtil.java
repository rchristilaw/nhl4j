package com.nhl4j.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class JsonUtil {

    public static Stream<JsonNode> streamOf(JsonNode node) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.elements(), 0), false);
    }

    public static String getStringFromNode(JsonNode node, String nodeName) {
        if (node == null) {
            return null;
        }

        if (!node.has(nodeName)) {
            return null;
        }

        return node.get(nodeName).asText();
    }

    public static JsonNode getFirstNodeFromArrayByKey(JsonNode arrayNode, String matcherKey, String matcherValue) {
        if (arrayNode == null || !arrayNode.isArray()) {
            return null;
        }

        return streamOf(arrayNode)
                .filter(node -> matcherValue.equals(node.get(matcherKey).textValue()))
                .findFirst().orElse(null);
    }

    public static List<JsonNode> getListFromNode(JsonNode node, String nodeName) {
        if (node == null) {
            return null;
        }

        if (!node.has(nodeName)) {
            return null;
        }

        if (!node.get(nodeName).isArray()) {
            return null;
        }

        return streamOf(node.get(nodeName)).toList();
    }

}
