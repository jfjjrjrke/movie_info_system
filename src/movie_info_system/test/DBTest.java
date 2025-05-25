package movie_info_system.test;

import java.sql.Connection;

import movie_info_system.dao.DBUtil;

public class DBTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (Connection conn = DBUtil.getConnection()) {
            if (conn != null) {
                System.out.println("✅ DB 연결 성공!");
            } else {
                System.out.println("❌ DB 연결 실패!");
            }
        } catch (Exception e) {
            System.out.println("❌ 예외 발생: " + e.getMessage());
        }
	}

}
