package org.manalith.ircbot.plugin.DistroPkgFinder;

import java.util.ArrayList;

public class PkgTable extends ArrayList<PkgTableUnit> {

	private static final long serialVersionUID = 1L;
	private int totalcnt;
	
	public PkgTable ()
	{
		super ();
		totalcnt = 0;
	}
	
	public void addElement (String GroupName, PkgUnit newPkgUnit)
	{
		boolean isAdded = false;
		if ( this.size() == 0 )
		{
			PkgTableUnit newPkgTableUnit = new PkgTableUnit(GroupName);
			newPkgTableUnit.addElement(newPkgUnit);
			this.add(newPkgTableUnit);
		}
		else
		{
			int size = this.size();
			
			for ( int i = 0 ; i < size ; i++ )
			{
				if ( this.get(i).getGroupName().equals(GroupName) )
				{
					if ( totalcnt >= 3 )
					{
						this.get(i).incCount();
						isAdded = true;
					}
					else
					{
						this.get(i).addElement(newPkgUnit);
						isAdded = true;
					}
				}
			}
			
			if ( isAdded == false )
			{
				PkgTableUnit newPkgTableUnit = new PkgTableUnit(GroupName);
				newPkgTableUnit.addElement(newPkgUnit);
				this.add(newPkgTableUnit);

			}
		}
		totalcnt++;
	}
	
	public String toString()
	{
		String result = "";
		
		if ( totalcnt == 0 )
		{
			result = "There is no packages";
		}
		else
		{
			PkgTableUnit unit = this.get(0);
			// System.out.println(unit.getGroupName()); // for the test
			if ( unit.getGroupName().equals("Exact hits") )
			{
				result += this.get(0).toString();
			}
			
			
			/*
			int size = this.size();
			for ( int i = 0 ; i < size ; i++ )
			{
				if ( i != 0 )
					result += " | ";
				
				result += this.get(i).toString();
			}
			//*/
			
			
		}
		
		return result;
	}
}
