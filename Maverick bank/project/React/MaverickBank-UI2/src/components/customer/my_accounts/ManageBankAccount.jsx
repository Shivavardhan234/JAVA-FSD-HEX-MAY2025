import { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {  useNavigate, Outlet } from 'react-router-dom';
import { Link } from 'react-router-dom';
import { getBankAccount } from '../../../store/actions/BankAccountAction';

function ManageBankAccount() {
    const navigate = useNavigate();
    const [showBalance, setShowBalance] = useState(false);
    const dispatch =useDispatch();
    const accountId = localStorage.getItem("accountId");

    useEffect(()=>{
        getBankAccount(dispatch)(accountId);
    },[])

    const account =useSelector(state=> state.bankAccount.account);
    


    return (
        <div className="container-fluid px-0">
            <div className="bg-light rounded-3 w-100 mx-0 px-0" style={{ width: '100%', minHeight: '85vh' }}>

                {/* Breadcrumb */}
                <nav aria-label="breadcrumb" className="p-3">
                    <ol className="breadcrumb mb-0">
                        <li className="breadcrumb-item">
                            <span className="text-muted">My Accounts</span>
                        </li>
                        <li className="breadcrumb-item active" aria-current="page">
                            Manage Bank Account
                        </li>
                    </ol>
                </nav>

                {/* Account Details Card */}
                <div className="card mx-3">
                    <div className="card-header bg-dark text-white fw-semibold">
                        Account Details
                    </div>
                    <div className="card-body d-flex align-items-stretch">
                        <div
                            className="d-flex align-items-center justify-content-center"
                            style={{ width: '200px', marginLeft: '8px' }}  // move icon slightly right
                        >
                            <i className="bi bi-person-vcard text-secondary" style={{ fontSize: '7rem' }}></i>
                        </div>
                        {/* Right Details */}
                        <div>
                            <p><strong>Account Number:</strong> {account?.accountNumber}</p>
                            <p><strong>Account Name:</strong> {account?.accountName}</p>
                            <p><strong>Branch:</strong> {account?.branch?.branchName}</p>
                            <p><strong>IFSC:</strong> {account?.branch?.ifsc}</p>
                            <p><strong>Account Type:</strong> {account?.accountType?.accountType}</p>
                            {showBalance && (
                                <div className="bg-success bg-opacity-25 p-3 mt-3 rounded d-flex justify-content-between align-items-center">
                                    <span className="fw-semibold text-success">
                                        Account Balance: ₹{account?.balance}
                                    </span>
                                    <i
                                        className="bi bi-x-circle text-danger fs-5"
                                        style={{ cursor: 'pointer' }}
                                        onClick={() => setShowBalance(false)}
                                    ></i>
                                </div>
                            )}
                        </div>
                    </div>
                </div>



                <div className="card shadow-sm mt-4 border-0 mx-3">
                    <div className="card-body">
                        <div className="row g-4 text-center">
                            {/* Row 1 - 4 Buttons */}
                            {/* Check Balance */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-success rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }} onClick={() => setShowBalance(true)}>
                                    <i className="bi bi-eye fs-4"></i>
                                </button>
                                <p className="mt-2 ">Check Balance</p>
                            </div>

                            {/* Deposit Money */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }} onClick={() => { navigate('/customer/myAccounts/deposit'); }}>
                                    <i className="bi bi-cash-stack fs-4"></i>
                                </button>
                                <p className="mt-2 ">Deposit Money</p>
                            </div>

                            {/* Withdraw Money */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }} onClick={() => { navigate('/customer/myAccounts/withdraw'); }}>
                                    <i className="bi bi-cash fs-4"></i>
                                </button>
                                <p className="mt-2 ">Withdraw Money</p>
                            </div>

                            {/* Transfer Money */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }}onClick={() => { navigate('/customer/myAccounts/transferMoney'); }}>
                                    <i className="bi bi-arrow-left-right fs-4"></i>
                                </button>
                                <p className="mt-2 ">Transfer Money</p>
                            </div>

                            {/* Row 2 - 4 Buttons */}
                            {/* My Transactions */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }}onClick={() => { navigate('/customer/myAccounts/myTransactions'); }}>
                                    <i className="bi bi-clock-history fs-4"></i>
                                </button>
                                <p className="mt-2 ">My Transactions</p>
                            </div>

                            {/* Account Statement */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }}onClick={() => { navigate('/customer/myAccounts/statement'); }}>
                                    <i className="bi bi-file-text fs-4"></i>
                                </button>
                                <p className="mt-2 ">Account Statement</p>
                            </div>

                            {/* My Loans */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-secondary rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }}  onClick={() => { navigate('/customer/myAccounts/myLoans'); }}>
                                    <i className="bi bi-bank fs-4"></i>
                                </button>
                                <p className="mt-2 ">My Loans</p>
                            </div>

                            {/* Account Closing / Suspension */}
                            <div className="col-3 d-flex flex-column align-items-center">
                                <button className="btn btn-outline-danger rounded-circle d-flex justify-content-center align-items-center"
                                    style={{ width: '80px', height: '80px' }} onClick={() => { navigate('/customer/myAccounts/request'); }}>
                                    <i className="bi bi-slash-circle fs-4"></i>
                                </button>
                                <p className="mt-2 ">Account Closing / Suspension</p>
                            </div>
                        </div>
                    </div>
                </div>






            </div>
            <div>
                <Outlet />
            </div>
        </div>

    );
}

export default ManageBankAccount;