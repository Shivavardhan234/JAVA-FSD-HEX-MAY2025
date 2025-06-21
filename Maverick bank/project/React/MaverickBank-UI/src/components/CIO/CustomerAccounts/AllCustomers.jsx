import { useEffect, useState } from 'react';
import '../../../css/AllCustomer.css'
import { useSelector } from 'react-redux';




function AllCustomers() {

    let allCustomersResponse = useSelector(state => state.customer.allCustomers);

    return (
        <div className="all-customer-container" >
            {console.log(allCustomersResponse)}
            <div className='card'>
                <div className='card-header'style={{color:'grey'}}>
                    <h1>All Customers</h1>
                </div>
                <div className='card-body'>
                    {
                        allCustomersResponse.map((c, index) => (
                            <div key={index}>
                                <li>{c.name}</li>
                            </div>
                        ))

                    }
                </div>
                <div className='card-footer'style={{color:'grey'}}>
                    pagenation
                </div>

            </div>





        </div>
    );
}

export default AllCustomers;