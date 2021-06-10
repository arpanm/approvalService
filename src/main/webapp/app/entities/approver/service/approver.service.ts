import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApprover, getApproverIdentifier } from '../approver.model';

export type EntityResponseType = HttpResponse<IApprover>;
export type EntityArrayResponseType = HttpResponse<IApprover[]>;

@Injectable({ providedIn: 'root' })
export class ApproverService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/approvers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(approver: IApprover): Observable<EntityResponseType> {
    return this.http.post<IApprover>(this.resourceUrl, approver, { observe: 'response' });
  }

  update(approver: IApprover): Observable<EntityResponseType> {
    return this.http.put<IApprover>(`${this.resourceUrl}/${getApproverIdentifier(approver) as number}`, approver, { observe: 'response' });
  }

  partialUpdate(approver: IApprover): Observable<EntityResponseType> {
    return this.http.patch<IApprover>(`${this.resourceUrl}/${getApproverIdentifier(approver) as number}`, approver, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApprover>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApprover[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApproverToCollectionIfMissing(approverCollection: IApprover[], ...approversToCheck: (IApprover | null | undefined)[]): IApprover[] {
    const approvers: IApprover[] = approversToCheck.filter(isPresent);
    if (approvers.length > 0) {
      const approverCollectionIdentifiers = approverCollection.map(approverItem => getApproverIdentifier(approverItem)!);
      const approversToAdd = approvers.filter(approverItem => {
        const approverIdentifier = getApproverIdentifier(approverItem);
        if (approverIdentifier == null || approverCollectionIdentifiers.includes(approverIdentifier)) {
          return false;
        }
        approverCollectionIdentifiers.push(approverIdentifier);
        return true;
      });
      return [...approversToAdd, ...approverCollection];
    }
    return approverCollection;
  }
}
