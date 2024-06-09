import com.lordbao.ch13ex2useradmin.business.User;
import com.lordbao.ch13ex2useradmin.data.DBUtil;
import jakarta.persistence.*;
import org.junit.jupiter.api.Test;

/**
 * @Author Lord_Bao
 * @Date 2024/6/8 14:55
 * @Version 1.0
 */
public class TestJPA {


    @Test
    public void testFind() {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            User user = em.find(User.class, 101L);
            user.setFirstName("huh?");
            //You can not update the primary key, or an exception will arise
//        user.setUserId(102L);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            em.close();
        }
    }

    /**
     *  My conclusion:
     *  despite four states of an object, detached, new, removed and managed,
     *  you can simply reduce them to two states: managed and unmanaged.
     *  take Object obj for instance,
     *  if obj is managed, then calling persist(obj) is fine,nothing will happen.
     *  if obj is unmanaged, things become complicated,
     *      1 assume  obj  has a primary key , which has conflict with the database,
     *      then an exception will happen if you call persist(obj)
     *      2 assume  obj  has a primary key having no conflict or obj has no primary key(
     *      the usual case is new), then it insert an item to database if you call persist(obj)
     * */
    @Test
    public void testPersist(){


        /*test new and managed state*/
//        EntityManager em = DBUtil.getEmFactory().createEntityManager();
//        EntityTransaction transaction = em.getTransaction();
//        transaction.begin();
//        try{
//           //now the user is new
//            User user = new User();
//            user.setFirstName("jack");
//
//            em.persist(user);
//            //note that user  is a managed entity now,you can get its id
//            System.out.println(user.getUserId());
//            //true,user is managed
//            System.out.println(em.contains(user));
//            //nothing will happen , if you pass a managed entity as parameter
//            em.persist(user);
//
//            transaction.commit();
//        }catch (Exception e){
//            e.printStackTrace();
//            transaction.rollback();
//        }finally {
//            em.close();
//        }


        /* test removed state
        *  try debug mode, see the differences after commiting the first and second transaction
        *  in database
        * */
//        EntityManager em = DBUtil.getEmFactory().createEntityManager();
//
//        try{
//            EntityTransaction transaction = em.getTransaction();
//            transaction.begin();
//            User user=null;
//            try{
//
//                //by invoking find method, you will  get  a user which is a managed entity
//                user = em.find(User.class, 1251L);
//                System.out.println(em.contains(user));//true
//                //after remove is called, user is removed now
//                em.remove(user);
//                transaction.commit();
//            }catch (Exception e){
//                e.printStackTrace();
//                transaction.rollback();
//            }
//
//            transaction = em.getTransaction();
//            transaction.begin();
//            try{
//                //note that user is a removed object
//                user.setLastName("jojo");
//                System.out.println(em.contains(user));//user now is not managed
//                em.persist(user);
//                System.out.println(em.contains(user));//user is managed again
//                transaction.commit();
//            }catch (Exception e){
//                e.printStackTrace();
//                transaction.rollback();
//            }
//        }finally {
//            em.close();
//        }

        /* test detach state.*/
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            User user = em.find(User.class, 1251L);
            //the user became detached
            em.detach(user);

            //case1: an exception will happen,if you directly call em.persistence(user)
            // the root cause is primary key is duplicated.
//            em.merge(user)

            //case2: after merging, and pass anotherUser(which is managed now).
            // Do not pass user, since it still is detached
//            User anotherUser = em.merge(user);
//            em.persist(anotherUser);//not passing user!!!

//          case 3: modify the primary key which has no duplicate in database
//          of the detached object,then it's ok to call    em.persist(user)
            user.setUserId(1252L);
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    public void testMerge(){
        //test new
//        EntityManager em = DBUtil.getEmFactory().createEntityManager();
//
//        EntityTransaction transaction = em.getTransaction();
//
//        try{
//            User user = new User();
//            transaction.begin();
//            User managedUser = em.merge(user);
//            System.out.println(em.contains(user));
//            System.out.println(em.contains(managedUser));
//            managedUser.setFirstName("jimmy");
//
//            transaction.commit();
//
//        }catch (Exception e){
//            e.printStackTrace();
//            transaction.rollback();
//        }finally {
//            em.close();
//        }

        //test detached
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();
            User user = em.find(User.class,1252L);
            em.detach(user);
            User managedUser = em.merge(user);
            System.out.println(em.contains(user));
            System.out.println(em.contains(managedUser));
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            em.close();
        }
    }



    @Test
    public void testRemove(){
        //new,managed is omitted here

        //test detached
//        EntityManager em = DBUtil.getEmFactory().createEntityManager();
//        User user = em.find(User.class, 1252L);
//        EntityTransaction transaction = em.getTransaction();
//        transaction.begin();
//        try{
//            em.detach(user);
//            //an exception will arise
//            em.remove(user);
//            transaction.commit();
//        }catch (Exception e){
//            e.printStackTrace();
//            transaction.rollback();
//        }finally {
//            em.close();
//        }

        // test removed state
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        try{
            User user = em.find(User.class, 1252L);
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();
            try{
                em.remove(user);
                transaction.commit();
            }catch (Exception e){
                e.printStackTrace();
                transaction.rollback();
            }

            transaction = em.getTransaction();
            transaction.begin();
            try{
                System.out.println(em.contains(user));
                em.remove(user);
                transaction.commit();
            }catch (Exception e){
                e.printStackTrace();
                transaction.rollback();
            }

        } finally {
            em.close();
        }


    }


    @Test
    public void testFlushAndCommit(){
        EntityManagerFactory factory = DBUtil.getEmFactory();
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try{

            transaction.begin();
            User user = em.find(User.class, 351L);
            user.setLastName("jojoland");


            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
        }finally {
            em.close();
        }

    }

}
