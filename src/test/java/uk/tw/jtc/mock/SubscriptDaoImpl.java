package uk.tw.jtc.mock;

import lombok.Getter;
import lombok.Setter;
import uk.tw.jtc.dao.SubscriptDao;
import uk.tw.jtc.model.Subscript;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class SubscriptDaoImpl implements SubscriptDao {
    private List<Subscript> subscriptList;

    @Override
    public void createNewSubscript(Subscript billing) {
        subscriptList.add(billing);
    }

    @Override
    public Subscript getSubscriptByCustomerId(String customerId) {
        Optional<Subscript> billing = subscriptList.stream().filter(e -> e.getCustomerId().equals(customerId)).findFirst();
        if(!billing.isPresent()){
            return null;
        }
        return billing.get();
    }



    @Override
    public List<Subscript> getSubscriptList() {
        return subscriptList;
    }


}
