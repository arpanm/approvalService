import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ApprovalType } from 'app/entities/enumerations/approval-type.model';
import { Status } from 'app/entities/enumerations/status.model';
import { IApprovalRequest, ApprovalRequest } from '../approval-request.model';

import { ApprovalRequestService } from './approval-request.service';

describe('Service Tests', () => {
  describe('ApprovalRequest Service', () => {
    let service: ApprovalRequestService;
    let httpMock: HttpTestingController;
    let elemDefault: IApprovalRequest;
    let expectedResult: IApprovalRequest | IApprovalRequest[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ApprovalRequestService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        programId: 'AAAAAAA',
        type: ApprovalType.Job,
        approveCallBackUrl: 'AAAAAAA',
        rejectCallBackUrl: 'AAAAAAA',
        createdBy: 'AAAAAAA',
        createdAt: currentDate,
        updatedBy: 'AAAAAAA',
        updatedAt: currentDate,
        finalStatus: Status.INIT,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_FORMAT),
            updatedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ApprovalRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_FORMAT),
            updatedAt: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.create(new ApprovalRequest()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ApprovalRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            programId: 'BBBBBB',
            type: 'BBBBBB',
            approveCallBackUrl: 'BBBBBB',
            rejectCallBackUrl: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdAt: currentDate.format(DATE_FORMAT),
            updatedBy: 'BBBBBB',
            updatedAt: currentDate.format(DATE_FORMAT),
            finalStatus: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ApprovalRequest', () => {
        const patchObject = Object.assign(
          {
            createdBy: 'BBBBBB',
          },
          new ApprovalRequest()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ApprovalRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            programId: 'BBBBBB',
            type: 'BBBBBB',
            approveCallBackUrl: 'BBBBBB',
            rejectCallBackUrl: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdAt: currentDate.format(DATE_FORMAT),
            updatedBy: 'BBBBBB',
            updatedAt: currentDate.format(DATE_FORMAT),
            finalStatus: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            updatedAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ApprovalRequest', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addApprovalRequestToCollectionIfMissing', () => {
        it('should add a ApprovalRequest to an empty array', () => {
          const approvalRequest: IApprovalRequest = { id: 123 };
          expectedResult = service.addApprovalRequestToCollectionIfMissing([], approvalRequest);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalRequest);
        });

        it('should not add a ApprovalRequest to an array that contains it', () => {
          const approvalRequest: IApprovalRequest = { id: 123 };
          const approvalRequestCollection: IApprovalRequest[] = [
            {
              ...approvalRequest,
            },
            { id: 456 },
          ];
          expectedResult = service.addApprovalRequestToCollectionIfMissing(approvalRequestCollection, approvalRequest);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ApprovalRequest to an array that doesn't contain it", () => {
          const approvalRequest: IApprovalRequest = { id: 123 };
          const approvalRequestCollection: IApprovalRequest[] = [{ id: 456 }];
          expectedResult = service.addApprovalRequestToCollectionIfMissing(approvalRequestCollection, approvalRequest);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalRequest);
        });

        it('should add only unique ApprovalRequest to an array', () => {
          const approvalRequestArray: IApprovalRequest[] = [{ id: 123 }, { id: 456 }, { id: 7262 }];
          const approvalRequestCollection: IApprovalRequest[] = [{ id: 123 }];
          expectedResult = service.addApprovalRequestToCollectionIfMissing(approvalRequestCollection, ...approvalRequestArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const approvalRequest: IApprovalRequest = { id: 123 };
          const approvalRequest2: IApprovalRequest = { id: 456 };
          expectedResult = service.addApprovalRequestToCollectionIfMissing([], approvalRequest, approvalRequest2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalRequest);
          expect(expectedResult).toContain(approvalRequest2);
        });

        it('should accept null and undefined values', () => {
          const approvalRequest: IApprovalRequest = { id: 123 };
          expectedResult = service.addApprovalRequestToCollectionIfMissing([], null, approvalRequest, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalRequest);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
