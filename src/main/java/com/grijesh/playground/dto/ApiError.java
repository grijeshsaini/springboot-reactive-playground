package com.grijesh.playground.dto;

/**
 * DTO which store error details.
 *
 * @author Grijesh Saini
 */
public record ApiError(int status, String reason) {
}
