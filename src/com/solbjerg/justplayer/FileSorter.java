package com.solbjerg.justplayer;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

public class FileSorter implements Comparator<File> {
	@Override
	public int compare(File lhs,
							 File rhs)
	{
		if (lhs == null)
		{
			return -1;
		}
		if (rhs == null)
		{
			return 1;
		}

		if (lhs.isDirectory())
		{
			if (rhs.isDirectory())
			{
				return nameCompare(lhs, rhs);
			} else
			{
				return -1;
			}
		} else
		{
			if (rhs.isDirectory())
			{
				return 1;
			} else
			{
				return nameCompare(lhs, rhs);
			}
		}
	}

	private int nameCompare(File lhs,
									File rhs)
	{
		return Collator.getInstance().compare(lhs.getAbsolutePath(), rhs.getAbsolutePath());
	}

}
