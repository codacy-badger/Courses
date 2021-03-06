/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Vladimir
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.devproserv.courses.validation.rules;

import com.devproserv.courses.validation.results.VldResult;
import com.devproserv.courses.validation.results.VldResultInvalid;
import com.devproserv.courses.validation.results.VldResultValid;

/**
 * Checks if string contains only digits.
 *
 * @since 0.5.0
 */
public final class VldRuleContainsDigits implements VldRule {
    /**
     * Message.
     */
    private final String message;

    /**
     * Primary constructor.
     * @param message Message
     */
    public VldRuleContainsDigits(final String message) {
        this.message = message;
    }

    @Override
    public VldResult apply(final String param) {
        final VldResult result;
        if (param.matches(".*\\D+.*")) {
            result = new VldResultInvalid(this.message);
        } else {
            result = new VldResultValid();
        }
        return result;
    }
}
