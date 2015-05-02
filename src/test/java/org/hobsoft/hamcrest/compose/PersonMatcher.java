/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hobsoft.hamcrest.compose;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.Matchers.is;

/**
 * 
 */
public class PersonMatcher extends TypeSafeMatcher<Person>
{
	private final List<Matcher<Person>> matchers;
	
	public PersonMatcher(Person expected)
	{
		matchers = new ArrayList<Matcher<Person>>();
		
		matchers.add(new FeatureMatcher<Person, String>(is(expected.getTitle()), "title", "title")
		{
			@Override
			protected String featureValueOf(Person actual)
			{
				return actual.getTitle();
			}
		});
		
		matchers.add(new FeatureMatcher<Person, String>(is(expected.getFirstName()), "firstName", "firstName")
		{
			@Override
			protected String featureValueOf(Person actual)
			{
				return actual.getFirstName();
			}
		});
		
		matchers.add(new FeatureMatcher<Person, String>(is(expected.getLastName()), "lastName", "lastName")
		{
			@Override
			protected String featureValueOf(Person actual)
			{
				return actual.getLastName();
			}
		});
	}

	public static Matcher<Person> personEqualTo(Person expected)
	{
		return new PersonMatcher(expected);
	}

	public void describeTo(Description description)
	{
		description.appendList("", " and ", "", matchers);
	}

	@Override
	protected boolean matchesSafely(Person actual)
	{
		for (Matcher<Person> matcher : matchers)
		{
			if (!matcher.matches(actual))
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	protected void describeMismatchSafely(final Person actual, Description mismatch)
	{
		List<SelfDescribing> mismatches = new ArrayList<SelfDescribing>();
		
		for (final Matcher<Person> matcher : matchers)
		{
			if (!matcher.matches(actual))
			{
				mismatches.add(new SelfDescribing()
				{
					public void describeTo(Description mismatch)
					{
						matcher.describeMismatch(actual, mismatch);
					}
				});
			}
		}
		
		mismatch.appendList("", " and ", "", mismatches);
	}
}