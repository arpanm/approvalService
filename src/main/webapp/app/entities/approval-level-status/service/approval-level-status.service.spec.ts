import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Status } from 'app/entities/enumerations/status.model';
import { IApprovalLevelStatus, ApprovalLevelStatus } from '../approval-level-status.model';

import { ApprovalLevelStatusService } from './approval-level-status.service';

describe('Service Tests', () => {
  describe('ApprovalLevelStatus Service', () => {
    let service: ApprovalLevelStatusService;
    let httpMock: HttpTestingController;
    let elemDefault: IApprovalLevelStatus;
    let expectedResult: IApprovalLevelStatus | IApprovalLevelStatus[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ApprovalLevelStatusService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        status: Status.INIT,
        level: 0,
        clientTime: 'AAAAAAA',
        createdBy: 'AAAAAAA',
        createdAt: currentDate,
        updatedBy: 'AAAAAAA',
        updatedAt: currentDate,
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

      it('should create a ApprovalLevelStatus', () => {
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

        service.create(new ApprovalLevelStatus()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ApprovalLevelStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            level: 1,
            clientTime: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdAt: currentDate.format(DATE_FORMAT),
            updatedBy: 'BBBBBB',
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ApprovalLevelStatus', () => {
        const patchObject = Object.assign(
          {
            level: 1,
            clientTime: 'BBBBBB',
            createdAt: currentDate.format(DATE_FORMAT),
            updatedAt: currentDate.format(DATE_FORMAT),
          },
          new ApprovalLevelStatus()
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

      it('should return a list of ApprovalLevelStatus', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            status: 'BBBBBB',
            level: 1,
            clientTime: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdAt: currentDate.format(DATE_FORMAT),
            updatedBy: 'BBBBBB',
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ApprovalLevelStatus', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addApprovalLevelStatusToCollectionIfMissing', () => {
        it('should add a ApprovalLevelStatus to an empty array', () => {
          const approvalLevelStatus: IApprovalLevelStatus = { id: 123 };
          expectedResult = service.addApprovalLevelStatusToCollectionIfMissing([], approvalLevelStatus);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalLevelStatus);
        });

        it('should not add a ApprovalLevelStatus to an array that contains it', () => {
          const approvalLevelStatus: IApprovalLevelStatus = { id: 123 };
          const approvalLevelStatusCollection: IApprovalLevelStatus[] = [
            {
              ...approvalLevelStatus,
            },
            { id: 456 },
          ];
          expectedResult = service.addApprovalLevelStatusToCollectionIfMissing(approvalLevelStatusCollection, approvalLevelStatus);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ApprovalLevelStatus to an array that doesn't contain it", () => {
          const approvalLevelStatus: IApprovalLevelStatus = { id: 123 };
          const approvalLevelStatusCollection: IApprovalLevelStatus[] = [{ id: 456 }];
          expectedResult = service.addApprovalLevelStatusToCollectionIfMissing(approvalLevelStatusCollection, approvalLevelStatus);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalLevelStatus);
        });

        it('should add only unique ApprovalLevelStatus to an array', () => {
          const approvalLevelStatusArray: IApprovalLevelStatus[] = [{ id: 123 }, { id: 456 }, { id: 78019 }];
          const approvalLevelStatusCollection: IApprovalLevelStatus[] = [{ id: 123 }];
          expectedResult = service.addApprovalLevelStatusToCollectionIfMissing(approvalLevelStatusCollection, ...approvalLevelStatusArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const approvalLevelStatus: IApprovalLevelStatus = { id: 123 };
          const approvalLevelStatus2: IApprovalLevelStatus = { id: 456 };
          expectedResult = service.addApprovalLevelStatusToCollectionIfMissing([], approvalLevelStatus, approvalLevelStatus2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(approvalLevelStatus);
          expect(expectedResult).toContain(approvalLevelStatus2);
        });

        it('should accept null and undefined values', () => {
          const approvalLevelStatus: IApprovalLevelStatus = { id: 123 };
          expectedResult = service.addApprovalLevelStatusToCollectionIfMissing([], null, approvalLevelStatus, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(approvalLevelStatus);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
