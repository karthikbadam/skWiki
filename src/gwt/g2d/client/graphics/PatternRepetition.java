/*
 * Copyright 2009 Hao Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.g2d.client.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * Determines the repetition mode of a pattern.
 * 
 * @author hao1300@gmail.com
 */
public enum PatternRepetition {
	REPEAT("repeat"), 
	REPEAT_X("repeat-x"), 
	REPEAT_Y("repeat-y"),
	NO_REPEAT("no-repeat");
	
	private static Map<String, PatternRepetition> repetitionMap;
	private final String repetitionName;
	
	private PatternRepetition(String repetitionName) {
		this.repetitionName = repetitionName;
	}
	
	@Override
	public String toString() {
		return repetitionName;
	}
	
	/**
	 * Parses a string into a PatternRepetition.
	 * 
	 * @param repetition
	 */
	public static PatternRepetition parseTextBaseline(String repetition) {
		if (repetitionMap == null) {
			repetitionMap = new HashMap<String, PatternRepetition>();
			for (PatternRepetition v : values()) {
				repetitionMap.put(v.toString(), v);
			}
		}
		return repetitionMap.get(repetition);
	}
}