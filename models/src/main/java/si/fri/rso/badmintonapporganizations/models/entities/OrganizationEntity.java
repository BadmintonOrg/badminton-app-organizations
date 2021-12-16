package si.fri.rso.badmintonapporganizations.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "badminton_organizations")
@NamedQueries(value =
        {
                @NamedQuery(name = "OrganizationEntity.getAll",
                        query = "SELECT oe FROM OrganizationEntity oe")
        })
public class OrganizationEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "name")
        private String name;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }
}
