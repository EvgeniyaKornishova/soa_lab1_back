package ru.itmo.operations;

import  ru.itmo.datasource.HibernateDatasource;
import  ru.itmo.entities.DBLocation;
import  ru.itmo.entities.DBPerson;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ru.itmo.PersonFilter;
import ru.itmo.pagination.Paginator;
import ru.itmo.sort.PersonSorter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PersonOperations {
    public long createPerson(DBPerson person) {
        Transaction transaction = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(person);

            transaction.commit();
            return person.getId();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Optional<DBPerson> getPerson(long id) {
        DBPerson dbPerson = null;
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {

            dbPerson = session.find(DBPerson.class, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(dbPerson);
    }

    public List<DBPerson> listPerson(PersonFilter filter, PersonSorter sorter, Paginator paginator) {
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            CriteriaBuilder criteria = session.getCriteriaBuilder();
            CriteriaQuery<DBPerson> criteria_query = criteria.createQuery(DBPerson.class);
            Root<DBPerson> root = criteria_query.from(DBPerson.class);

            filter.prepareFilter(criteria, criteria_query, root);

            sorter.prepareSort(criteria, criteria_query, root);

            Query<DBPerson> query = session.createQuery(criteria_query);

            paginator.preparePaginator(query);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public Long countPersons(){
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            CriteriaBuilder criteria = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteria_query = criteria.createQuery(Long.class);
            Root<DBPerson> root = criteria_query.from(DBPerson.class);

            criteria_query.select(criteria.count(root));
            Query<Long> query = session.createQuery(criteria_query);

            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public void updatePerson(DBPerson dbPerson) {
        Transaction transaction = null;

        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.update(dbPerson);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public boolean deletePerson(long id) {
        Transaction transaction = null;

        boolean isSuccessful = false;

        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            DBPerson dbPerson = session.find(DBPerson.class, id);

            if (dbPerson != null) {
                session.delete(dbPerson);
                session.flush();
                isSuccessful = true;
            }
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return isSuccessful;
    }

    public double calcSumHeight(){
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            CriteriaBuilder criteria = session.getCriteriaBuilder();
            CriteriaQuery<Double> criteria_query = criteria.createQuery(Double.class);
            Root<DBPerson> root = criteria_query.from(DBPerson.class);

            criteria_query.select(criteria.sumAsDouble(root.get("height")));

            return session.createQuery(criteria_query).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<DBPerson> searchByName(String substring) {
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            CriteriaBuilder criteria = session.getCriteriaBuilder();
            CriteriaQuery<DBPerson> criteria_query = criteria.createQuery(DBPerson.class);
            Root<DBPerson> root = criteria_query.from(DBPerson.class);

            String search_expression = "%" + substring + "%";
            criteria_query.where(criteria.like(root.get("name"), search_expression));

            return session.createQuery(criteria_query).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private boolean compare(DBLocation left, DBLocation right){
        return (Float.compare(left.getY(), right.getY()) == 0 && Float.compare(left.getZ(), right.getZ()) == 0 && Objects.equals(left.getX(), right.getX()));
    }

    public List<DBLocation> listUniqLocations() {
        try (Session session = HibernateDatasource.getSessionFactory().openSession()) {
            CriteriaQuery<DBLocation> criteria_query = session.getCriteriaBuilder().createQuery(DBLocation.class);
            criteria_query.from(DBLocation.class);

            List<DBLocation> locations = session.createQuery(criteria_query).getResultList();
            List<DBLocation> uniq_locations = new java.util.ArrayList<>(List.of());

            for (DBLocation loc : locations){
                boolean flag = true;
                for (DBLocation uniq_loc : uniq_locations){
                    if (compare(loc, uniq_loc))
                        flag = false;
                }
                if (flag)
                    uniq_locations.add(loc);
            }

            return uniq_locations;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }


}
