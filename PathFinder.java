	import java.util.Scanner;
	import java.util.StringTokenizer;
	import java.util.Hashtable;

	public class PathFinder {
		DoubleHash cache;
		DataCenterMap map;
		long timeHashing;
		int counter;
		
		public PathFinder(DataCenterMap map) {
			this.map = map;
		}
		
		public static void main(String[] args) {
			DataCenterMap map = readInput();
			PathFinder p = new PathFinder(map);
			measurePerformance(p);
		}
		
		public static void measurePerformance(PathFinder p) {
			
			long start, end;
			int paths;
			
			/**
			start = System.currentTimeMillis();
			paths = p.countPaths();
			end = System.currentTimeMillis();
			
			

			System.out.println("Naive Method:");
			System.out.println("Found " + paths + " paths");
			System.out.println("Took " + (end - start) + " milliseconds");
			System.out.println("Recursively called " + p.counter + " times");
			System.out.println();
			*/
			
			start = System.currentTimeMillis();
			paths = p.countPathsShort();
			end = System.currentTimeMillis();

			
			System.out.println("Optimized Method:");
			System.out.println("Found " + paths + " paths");
			System.out.println("Took " + (end - start) + " milliseconds");
			System.out.println("Recursively called " + p.counter + " times");
			System.out.println();
		}
		
		public int countPaths() {
			counter = 0;
			return countPathsHelper(map.start[0], map.start[1], true);
		}
		
		public int countPathsHelper(int w, int h, boolean firstTime) {
			counter++;

			if(map.badPos(w, h, firstTime)) {return 0;}
			
			if(map.winningPos(w, h)) {return 1;}
			
			map.setSquare(w, h, 4);
			int sum;
			sum = countPathsHelper(w + 1, h, false) + countPathsHelper(w - 1, h, false) + 
					countPathsHelper(w, h + 1, false) + countPathsHelper(w, h - 1, false);
			
			map.setSquare(w, h, 0);
			return sum;
			
		}
		
		public int countPathsShort() {
			counter = 0;
			return shortHelper(map.start[0], map.start[1], true);
		}
		
		public int shortHelper(int w, int h, boolean firstTime) {
			counter++;
			
			//System.out.println("Currently looking at: (" + w + ", " + h + ")");
			//System.out.println(map);
			//map.printNums();
			
			if(map.badPos(w,  h, firstTime) || !map.checkDegree(w, h)) {
				return 0;
			}
			
			if(map.winningPos(w, h)) {
				return 1;
			}
			
			map.setSquare(w, h, 4);
			int sum;
			sum = shortHelper(w + 1, h, false) + shortHelper(w - 1, h, false) +
					shortHelper(w, h + 1, false) + shortHelper(w, h - 1, false);
			
			map.setSquare(w, h, 0);
			
			return sum;
		}
		
		public int countPathsOptimized() {
			counter = 0;
			timeHashing = 0;
			cache = new DoubleHash();
			int sum = optimizedHelper(0, 0, true);
			cache = new DoubleHash();
			return sum;
		}
		
		public int optimizedHelper(int w, int h, boolean firstTime) {
			counter++;
			if(map.badPos(w, h, firstTime)) {return 0;}
			if(map.winningPos(w, h)) {return 1;}
			
			long startTime, endTime;
			
			map.setSquare(w, h, 4);

			if(cache.containsKey(map, w, h)) {
				int temp = (Integer)cache.get(map, w, h);
				startTime = System.currentTimeMillis();					
				map.setSquare(w, h, 0);
				endTime = System.currentTimeMillis();
				timeHashing += (endTime - startTime);
				return temp;
			}
			
			int sum;
			
			sum = optimizedHelper(w + 1, h, false) + optimizedHelper(w - 1, h, false) + 
					optimizedHelper(w, h + 1, false) + optimizedHelper(w, h - 1, false);
			
			startTime = System.currentTimeMillis();
			cache.put(map, w, h, sum); 
			endTime = System.currentTimeMillis();
			timeHashing += (endTime - startTime);
			
			map.setSquare(w, h, 0);
			return sum;
		}
		
		public static DataCenterMap readInput() {
			DataCenterMap map;
			
			Scanner sc = new Scanner(System.in).useDelimiter("\n");
			String input = sc.next();
			StringTokenizer st = new StringTokenizer(input);
			
			int width = Integer.parseInt(st.nextToken());
			int height = Integer.parseInt(st.nextToken());
			
			map = new DataCenterMap(width, height);
			
			for(int j = 0; j < height; j++) {
				for(int i = 0; i < width; i++) {
					map.setSquare(i, j, Integer.parseInt(st.nextToken()));
				}
			}
			
			System.out.println(map);
			System.out.println("The start is at: (" + map.start[0] + ", " + map.start[1] + ")");
			System.out.println("The end is at: (" + map.end[0] + ", " + map.end[1] + ")");
			map.printNums();
			
			return map;
		}
	}

